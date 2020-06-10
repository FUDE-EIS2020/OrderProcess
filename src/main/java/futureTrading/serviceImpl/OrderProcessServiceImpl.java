package futureTrading.serviceImpl;

import futureTrading.dto.OrderInMDDto;
import futureTrading.entities.*;
import futureTrading.repositories.BrokerRepo;
import futureTrading.repositories.FuturesOrderRepo;
import futureTrading.repositories.FuturesProductRepo;
import futureTrading.repositories.TraderRepo;
import futureTrading.service.IDService;
import futureTrading.service.MarketDepthService;
import futureTrading.service.OrderProcessService;
import futureTrading.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private MarketDepthService marketDepthService;

    @Autowired
    private BrokerRepo brokerRepo;

    @Autowired
    private TraderRepo traderRepo;

    @Autowired
    private FuturesProductRepo futuresProductRepo;

    @Autowired
    private FuturesOrderRepo futuresOrderRepo;

    @Autowired
    private IDService idService;

    @Autowired
    private RedisService redisService;

    @Override
    public void processOrder(OrderInMDDto orderInMDDto) {
        switch (orderInMDDto.getOrderType()) {
            case "market":
                processMarketOrder(orderInMDDto, orderInMDDto.getBrokerId(), orderInMDDto.getProductId());
                break;
            case "limit":
                processLimitOrder(orderInMDDto, orderInMDDto.getBrokerId(), orderInMDDto.getProductId());
                break;
            case "stop":
                break;
            case "cancel":
                break;
            default:
        }

    }

    @Override
    public void notifyGateWay() {
        kafkaTemplate.send("test", "order XX processed");
    }

//    private void saveOrderTransaction() {
//    }

    private void saveOrderTransaction(String productId, Double price, Integer amount, String orderType, String brokerId, String buyerId, String sellerId, String initiator) {
        FuturesProduct product = futuresProductRepo.getById(Long.parseLong(productId));
        Trader seller = traderRepo.getById(Long.parseLong(sellerId));
        Trader buyer = traderRepo.getById(Long.parseLong(buyerId));
        Broker broker = brokerRepo.getById(Long.parseLong(brokerId));

        Long orderId = idService.generate("futuresOrder");

        FuturesOrder order = new FuturesOrder();
        order.setId(orderId);
        order.setProduct(product);
        order.setPrice(price);
        order.setAmount(amount);
        if (orderType.equals("market")) {
            order.setOrderType(FuturesOrder.OrderType.MARKET);
        }
        else if (orderType.equals("limit")) {
            order.setOrderType(FuturesOrder.OrderType.LIMIT);
        }
        else if (orderType.equals("stop")) {
            order.setOrderType(FuturesOrder.OrderType.STOP);
        }
        else {
            order.setOrderType(FuturesOrder.OrderType.CANCEL);
        }
        order.setOrderState(FuturesOrder.OrderState.FINISHED);
        order.setCreateTime(new Date());
        order.setFinishTime(new Date());
        order.setBroker(broker);
        order.setSeller(seller);
        order.setBuyer(buyer);
        if (initiator.equals("buyer")){
            order.setInitiator(FuturesOrder.OrderInitiator.BUYER);
        }
        else {
            order.setInitiator(FuturesOrder.OrderInitiator.SELLER);
        }

        futuresOrderRepo.save(order);

    }

    private void processMarketOrder(OrderInMDDto orderInMDDto, String brokerId, String productId) {
        switch (orderInMDDto.getType()) {
            case "buy":
                // 这种情况只修改队列里的卖单，先保留别的订单
                List<OrderInMD> sellOrders = marketDepthService.getSellOrdersInMD(brokerId, productId);
                List<OrderInMD> remainOrders = marketDepthService.getBuyOrdersInMD(brokerId, productId);
                remainOrders.addAll(marketDepthService.getMarketOrdersInMD(brokerId, productId));
                remainOrders.addAll(marketDepthService.getStopOrdersInMD(brokerId, productId));
                // 记录下这笔订单的数量
                Integer remainingAmount = orderInMDDto.getAmount();
                for (OrderInMD order : sellOrders) {
                    Integer amount = order.getAmount();
                    if (amount > remainingAmount) {
                        // 当订单可以满足这次的market order
                        order.setAmount(amount - remainingAmount);
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), remainingAmount,
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), orderInMDDto.getTraderId(), order.getTraderId(),
                                "seller");
                        remainingAmount = 0;

                        break;
                    } else if (amount.equals(remainingAmount)) {
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), remainingAmount,
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), orderInMDDto.getTraderId(), order.getTraderId(),
                                "seller");
                        sellOrders.remove(order);
                        remainingAmount = 0;

                        break;
                    } else {
                        // 这个订单数量少，应该接着往下面进行， 别忘了最后修改remaining amount
                        sellOrders.remove(order);
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), order.getAmount(),
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), orderInMDDto.getTraderId(), order.getTraderId(),
                                "seller");
                        remainingAmount = remainingAmount - order.getAmount();
                    }
                }

                if (remainingAmount > 0) {
                    // save market order
                    OrderInMD orderInMD = new OrderInMD();
                    orderInMD.setId(idService.generate("orderInMD"));
                    orderInMD.setAmount(remainingAmount);
                    orderInMD.setPrice(orderInMDDto.getPrice());
                    orderInMD.setType("buy");
                    orderInMD.setTag("M");
                    orderInMD.setCreateTime(new Date());
                    orderInMD.setTraderId(orderInMDDto.getTraderId());
                    sellOrders.add(orderInMD);
                }
                // set orders in md
                sellOrders.addAll(remainOrders);
                redisService.setOrder(brokerId, productId, sellOrders);

                break;
            case "sell":
                // 这种情况处理队列中的买单， 先把别的订单拿出来保留
                List<OrderInMD> buyOrders = marketDepthService.getBuyOrdersInMD(brokerId, productId);
                List<OrderInMD> remainOrders1 = marketDepthService.getSellOrdersInMD(brokerId, productId);
                remainOrders1.addAll(marketDepthService.getMarketOrdersInMD(brokerId, productId));
                remainOrders1.addAll(marketDepthService.getStopOrdersInMD(brokerId, productId));
                // 记录这个订单的大小
                Integer remainingAmount1 = orderInMDDto.getAmount();
                for (OrderInMD order : buyOrders) {
                    Integer amount = order.getAmount();
                    if (amount > remainingAmount1) {
                        // 当订单可以满足这次的market order
                        order.setAmount(amount - remainingAmount1);
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), remainingAmount1,
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), order.getTraderId(), orderInMDDto.getTraderId(),
                                "buyer");
                        remainingAmount1 = 0;

                        break;
                    } else if (amount.equals(remainingAmount1)) {
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), remainingAmount1,
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), order.getTraderId(), orderInMDDto.getTraderId(),
                                "buyer");
                        buyOrders.remove(order);
                        remainingAmount1 = 0;

                        break;
                    } else {
                        // 这个订单数量少，应该接着往下面进行， 别忘了最后修改remaining amount
                        buyOrders.remove(order);
                        saveOrderTransaction(orderInMDDto.getProductId(), order.getPrice(), order.getAmount(),
                                orderInMDDto.getOrderType(), orderInMDDto.getBrokerId(), order.getTraderId(), orderInMDDto.getTraderId(),
                                "buyer");
                        remainingAmount1 = remainingAmount1 - order.getAmount();
                    }
                }

                if (remainingAmount1 > 0) {
                    // save market order
                    OrderInMD orderInMD = new OrderInMD();
                    orderInMD.setId(idService.generate("orderInMD"));
                    orderInMD.setAmount(remainingAmount1);
                    orderInMD.setPrice(orderInMDDto.getPrice());
                    orderInMD.setType("sell");
                    orderInMD.setTag("M");
                    orderInMD.setCreateTime(new Date());
                    orderInMD.setTraderId(orderInMDDto.getTraderId());
                    buyOrders.add(orderInMD);
                }
                // set orders in md
                buyOrders.addAll(remainOrders1);
                redisService.setOrder(brokerId, productId, buyOrders);

                break;
            default:
        }
    }

    private void processLimitOrder(OrderInMDDto orderInMDDto, String brokerId, String productId) {
        List<OrderInMD> sellOrders = marketDepthService.getSellOrdersInMD(brokerId, productId);
        List<OrderInMD> buyOrders = marketDepthService.getBuyOrdersInMD(brokerId, productId);
        switch (orderInMDDto.getType()) {
            case "buy":
                if (sellOrders.size() == 0 || sellOrders.get(0).getPrice() > orderInMDDto.getPrice()) {
                    // 当没有卖单 或者 卖单的最低价比这次买单的定价高（意思就是这个价钱买不到） 那么直接把这个order加入到list里
                    OrderInMD orderInMD = new OrderInMD();
                    orderInMD.setId(idService.generate("orderInMD"));
                    orderInMD.setAmount(orderInMDDto.getAmount());
                    orderInMD.setPrice(orderInMDDto.getPrice());
                    orderInMD.setCreateTime(new Date());
                    orderInMD.setTag("");
                    orderInMD.setTraderId(orderInMDDto.getTraderId());
                    orderInMD.setType("buy");

                    List<OrderInMD> orderInMDS = redisService.getOrder(brokerId, productId);
                    orderInMDS.add(orderInMD);
                    redisService.setOrder(brokerId, productId, orderInMDS);

                } else {
                    // 这种情况是挂的买单要比当前的最低售价高（或相等），需要进行交易

                    // 记录笔数
                    Integer remainingAmount = orderInMDDto.getAmount();
                    for (OrderInMD order : sellOrders) {
                        // 对于队列中所有的订单，把价格低于这次limit order的单全部买下来
                        if (order.getPrice() <= orderInMDDto.getPrice()) {
                            if (order.getAmount() > remainingAmount) {
                                saveOrderTransaction(productId, order.getPrice(), remainingAmount, "limit",
                                        brokerId, orderInMDDto.getTraderId(), order.getTraderId(), "seller");
                                order.setAmount(order.getAmount()-remainingAmount);
                                remainingAmount = 0;
                                break;
                            }
                            else if (order.getAmount().equals(remainingAmount)) {
                                saveOrderTransaction(productId, order.getPrice(), remainingAmount, "limit",
                                        brokerId, orderInMDDto.getTraderId(), order.getTraderId(), "seller");
                                sellOrders.remove(order);
                                remainingAmount = 0;
                                break;
                            }
                            else {
                                remainingAmount = remainingAmount - order.getAmount();
                                saveOrderTransaction(productId, order.getPrice(), order.getAmount(), "limit",
                                        brokerId, orderInMDDto.getTraderId(), order.getTraderId(), "seller");
                                sellOrders.remove(order);
                            }
                        }
                    }

                    // 处理结束后把内存中的队列重新保存，如果还有剩余还有加入相反的队列里
                    if (remainingAmount > 0) {
                        OrderInMD orderInMD = new OrderInMD();
                        orderInMD.setId(idService.generate("orderInMD"));
                        orderInMD.setAmount(remainingAmount);
                        orderInMD.setPrice(orderInMDDto.getPrice());
                        orderInMD.setType("buy");
                        orderInMD.setTag("");
                        orderInMD.setCreateTime(new Date());
                        orderInMD.setTraderId(orderInMDDto.getTraderId());

                        buyOrders.add(orderInMD);
                    }
                    buyOrders.addAll(sellOrders);
                    buyOrders.addAll(marketDepthService.getMarketOrdersInMD(brokerId, productId));
                    buyOrders.addAll(marketDepthService.getStopOrdersInMD(brokerId, productId));

                    redisService.setOrder(brokerId, productId, buyOrders);
                }
                break;
            case "sell":
                if (buyOrders.size() == 0 || buyOrders.get(0).getPrice() < orderInMDDto.getPrice()) {
                    // 当没有买单 或者当前买单的最高价不到这次的卖单（意思就是这个卖单卖不出去） 直接把order加入list
                    OrderInMD orderInMD = new OrderInMD();
                    orderInMD.setId(idService.generate("orderInMD"));
                    orderInMD.setAmount(orderInMDDto.getAmount());
                    orderInMD.setPrice(orderInMDDto.getPrice());
                    orderInMD.setCreateTime(new Date());
                    orderInMD.setTag("");
                    orderInMD.setTraderId(orderInMDDto.getTraderId());
                    orderInMD.setType("sell");

                    List<OrderInMD> orderInMDS = redisService.getOrder(brokerId, productId);
                    orderInMDS.add(orderInMD);
                    redisService.setOrder(brokerId, productId, orderInMDS);

                } else {
                    // 这种情况是挂的卖单比当前最高的买单高（或相等），需要进行交易

                    // 记录笔数
                    Integer remainingAmount1 = orderInMDDto.getAmount();
                    for (OrderInMD order:buyOrders) {
                        if (order.getAmount() > remainingAmount1) {
                            saveOrderTransaction(productId, order.getPrice(), remainingAmount1, "limit",
                                    brokerId, order.getTraderId(), orderInMDDto.getTraderId(),"buyer");
                            order.setAmount(order.getAmount()-remainingAmount1);
                            remainingAmount1 = 0;
                            break;
                        }
                        else if (order.getAmount().equals(remainingAmount1)) {
                            saveOrderTransaction(productId, order.getPrice(), remainingAmount1, "limit",
                                    brokerId, order.getTraderId(), orderInMDDto.getTraderId(),"buyer");
                            buyOrders.remove(order);
                            remainingAmount1 = 0;
                            break;
                        }
                        else {
                            saveOrderTransaction(productId, order.getPrice(), order.getAmount(), "limit",
                                    brokerId, order.getTraderId(), orderInMDDto.getTraderId(),"buyer");
                            remainingAmount1 = remainingAmount1 - order.getAmount();
                            buyOrders.remove(order);
                        }
                    }

                    if (remainingAmount1 > 0) {
                        OrderInMD orderInMD = new OrderInMD();
                        orderInMD.setId(idService.generate("orderInMD"));
                        orderInMD.setAmount(remainingAmount1);
                        orderInMD.setPrice(orderInMDDto.getPrice());
                        orderInMD.setType("sell");
                        orderInMD.setTag("");
                        orderInMD.setCreateTime(new Date());
                        orderInMD.setTraderId(orderInMDDto.getTraderId());

                        sellOrders.add(orderInMD);
                    }
                    sellOrders.addAll(buyOrders);
                    sellOrders.addAll(marketDepthService.getMarketOrdersInMD(brokerId, productId));
                    sellOrders.addAll(marketDepthService.getStopOrdersInMD(brokerId, productId));

                    redisService.setOrder(brokerId, productId, sellOrders);
                }
                break;
            default:
        }
    }

    private void processStopOrder(OrderInMDDto orderInMDDto) {

    }

    private void processCancelOrder() {

    }
}
