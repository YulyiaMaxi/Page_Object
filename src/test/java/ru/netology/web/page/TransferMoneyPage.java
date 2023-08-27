package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class TransferMoneyPage { // страница пополнения карт
    private final SelenideElement amountNew = $("[data-test-id = amount] input");
    private final SelenideElement cardFrom = $("[data-test-id = from] input");
    private final SelenideElement header = $("[data-test-id = dashboard]");
    private final SelenideElement errorMessage = $("[data-test-id = error-message]");
    private final SelenideElement transferButton = $("[data-test-id = action-transfer]");

    public TransferMoneyPage(){
        header.shouldBe(Condition.visible);
    }
    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo){ // здесь в целом к объекту датахелпера
        makeTransfer(amountToTransfer, cardInfo); // этот метод переиспользует мечтод makeTransfer, второй раз мы прописываем только переменные без типов переменных
        return new DashboardPage();
    }
    public void makeTransfer (String amountToTransfer, DataHelper.CardInfo cardInfo){ // здесь в целом к объекту датахелпера
       amountNew.setValue(amountToTransfer); // сначала пишем селенидовский элемент, затем метод вставки в него значения, в скобках - ту переменную, как она называется на входе в метод
       cardFrom.setValue(cardInfo.getCardNumber()); // здесь конкретно вызываем у объекта датахелпера опред.метод получения номера
transferButton.click();
    }
public void findErrorMessage (String expectedText){//когда мы не знаем, какой текст ждать в ошибке, то пишем expected text
    errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)) // не забываем про точку в duration.ofSeconds
                .shouldBe(visible);
}

}
