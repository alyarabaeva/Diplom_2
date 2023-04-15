package response_objects;

public class CreateOrderResponse {
    private boolean success;
    private String name;
    private OrderInfoResponse order;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderInfoResponse getOrder() {
        return order;
    }

    public void setOrder(OrderInfoResponse order) {
        this.order = order;
    }
}
