package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");

  private final ElementsCollection cards = $$(".list__item div");

  public DashboardPage() {
    heading.shouldBe(visible);
  }




  //public TransferMoneyPage selectCardToTransfer (DataHelper.CardInfo cardInfo) {
   // cards.findBy(Condition.text(cardInfo.getCardNumber().substring(15))).$("button").click(); // а здесь по последним цифрам номера карты ищем карту и нажимаем "Пополнить"
    //return new TransferMoneyPage();
  //}

  public TransferMoneyPage selectCardToTransfer (DataHelper.CardInfo cardInfo) {
    cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click(); // а здесь ищем по атрибуту, значение атрибута находится в той сущности, кот. мы передаем в метод

    return new TransferMoneyPage();
  }
  private final String balanceStart = "баланс: ";// поскольку сама цифра баланса лежит между словом "баланс" и "р.",то обрезаем по бокам эти слова, плюс двоеточие, точку, пробелы...
  private final String balanceFinish = " р.";

  public int getCardBalance(DataHelper.CardInfo cardInfo) {
    var text = cards.findBy(Condition.text(cardInfo.getCardNumber().substring(15))).getText();// по последним 4 цифрам ищем номер нужной карты и брем методом getText всю строку ***0001 баланс 10 000 р. Пополнить
    return extractBalance(text);

  }
  private int extractBalance(String text) {
    var start = text.indexOf(balanceStart); // ищем слово, начало которого (буква) лежит в индексе balanceStart
    var finish = text.indexOf(balanceFinish);// ищем слово, начало которого (буква) лежит в индексе balanceFinish
    var value = text.substring(start + balanceStart.length(), finish);// вырезаем текст, который лежит между индексом первойй сущности, всю длину первой сущности, до первого символа конечной сущности, то есть 1000
    return Integer.parseInt(value); //преобоазуем в число
  }


}








