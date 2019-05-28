## Welcome to FasterPay Java SDK

FasterPay Java SDK enables you to integrate the FasterPay's Checkout Page seamlessly without having the hassle of integrating everything from Scratch. Once your customer is ready to pay, FasterPay will take care of the payment, notify your system about the payment and return the customer back to your Thank You page.

### Add the SDK to your project

The FasterPay Java SDK is now available at [Maven Repository](https://repo1.maven.org/maven2/com/fasterpay/fasterpay-java-sdk/). The latest version is available via `mavenCentral()`:

```
<dependency>
  <groupId>com.fasterpay</groupId>
  <artifactId>fasterpay-java-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Initiating Payment Request

```java
public class TransactionServlet extends HttpServlet {

    private Gateway gateway = Gateway.builder()
        .publicApi("<your public key>")
        .privateApi("<your private key>")
        .build();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        
        Form form = gateway.paymentForm()
            .amount("5.00")
            .currency("USD")
            .description("Golden ticket")
            .merchantOrderId(UUID.randomUUID().toString())
            .sign_version(SignVersion.VERSION_2)
            .isAutoSubmit(true);

        writer.println("<html><body>");
        writer.println(form.build());
        writer.println("</body></html>");
    }
}
```

### Subscriptions request

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter writer = resp.getWriter();

    Form subscriptionsForm = gateway.subscriptionForm()
        .amount("1")
        .currency("USD")
        .description("Moonsoon festival")
        .merchantOrderId(UUID.randomUUID().toString())
        .recurringName("moonsoon")
        .recurringSkuId("festival")
        .recurringPeriod("3m")
        .sign_version(SignVersion.VERSION_2)
        .isAutoSubmit(true);

    writer.println("<html><body>");
    writer.println(subscriptionsForm.build());
    writer.println("</body></html>");
}
```

### Cancel subscriptions
```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter writer = resp.getWriter();

    String orderId = req.getParameter("orderId");
    Response result = gateway.cancelSubscription(orderId);

    writer.println("<html><body>");
    writer.println(Optional.of(result)
        .filter(response -> response.isSuccess())
        .map(response -> "Cancel order " + orderId + " successfully")
        .orElse("Cancel order " + orderId + " failed: " + result.getCode() + "- " + result.getMessage()));
    writer.println("</body></html>");
}
```

### Refund request
```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter writer = resp.getWriter();

    String orderId = req.getParameter("orderId");
    String amount = req.getParameter("amount");
    Response result = gateway.refund(orderId, Float.parseFloat(amount));
    writer.println("<html><body>");
    writer.println(Optional.of(result)
        .filter(response -> response.isSuccess())
        .map(response -> "Refund order " + orderId + " successfully")
        .orElse("Refund order " + orderId + " failed: " + result.getCode() + "- " + result.getMessage()));
    writer.println("</body></html>");
}
```

### Validate Pingback
```java
import spark.Service;

public class PingBackRoute implements Routes {
    
    private PingBack pingBack = new PingBack("<your private key");
    
    @Override
    public void define(Service service) {
        this.service = service;
        definePingbackRoutes();
    }

    @POST
    public void definePingbackRoutes() {
        service.post(BASE_PATH, ((request, response) -> {
            boolean isValid = pingBack.validation()
                .signVersion(Optional.ofNullable(request.headers(PingBack.X_FASTERPAY_SIGNATURE_VERSION)))
                .apiKey(Optional.ofNullable(request.headers(PingBack.X_API_KEY)))
                .signature(Optional.ofNullable(request.headers(PingBack.X_FASTERPAY_SIGNATURE)))
                .pingBackData(Optional.of(request.body()))
                .execute();
            if (isValid) {
                //process further with pingBack
                response.status(HttpStatus.OK_200);
                return response;
            } else {
                //process with invalid pingBack
                throw halt(HttpStatus.NOT_IMPLEMENTED_501);
            }
        }));
    }
}
```

### FasterPay Test Mode

FasterPay has a Sandbox environment called Test Mode. Test Mode is a virtual testing environment which is an exact replica of the live FasterPay environment. This allows businesses to integrate and test the payment flow without being in the live environment. Businesses can create a FasterPay account, turn on the **Test Mode** and begin to integrate the widget using the test integration keys.

#### Initiating FasterPay Gateway in Test-Mode

```java
private Gateway gateway = Gateway.builder()
    .publicApi("<your public key>")
    .privateApi("<your private key>")
    .isTest(true)
    .build();
```

### Questions?

- Common questions are covered in the [FAQ](https://www.fasterpay.com/support).
- For integration and API questions, feel free to reach out Integration Team via [integration@fasterpay.com](integration@fasterpay.com)
- For business support, email us at [merchantsupport@fasterpay.com](merchantsupport@fasterpay.com)
- To contact sales, email [sales@fasterpay.com](sales@fasterpay.com).