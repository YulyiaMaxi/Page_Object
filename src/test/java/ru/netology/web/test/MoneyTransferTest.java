package ru.netology.web.test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
    //DashboardPage dashboardPage; //если экземпляр класса используется не однократно, но лучше прописать его сразу в классе

    @BeforeEach
        // в предусловии выполняется часть сценария
    void setup() { // не забываем про метод setup, который устанавливает следующую логику шагов
        open("http://localhost:9999");

    }
    // выполняем верификацию и переходим в ЛК
// в результате работы validVerify возвращается новый объект страницы VerificationPage - новый dashboardpage, которы  мы сохраняем в переменную dashboardpage
    @Test
    void shouldTransferAmountFromFirstToSecondCard(){
        var loginPage = new LoginPage();// после открытия страницы объявляем новую сущность - страницу ввода логина и пароля
        var authInfo = getAuthInfo(); // получаем данные авторизации (логин и пароль) из датахелпера
        var verificationPage = loginPage.validLogin(authInfo);//вставляем данные авторизации
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // получаем код верификации и в результате открывается страница для ввода кода проверки
        var dashboardPage = verificationPage.validVerify(verificationCode);// вводим код и в результате открывается страница ЛК
        var firstCardInfo = DataHelper.getFirstCardInfo();// берем полную строку данных о карте
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);//первично вызываем методом и проставляем балансы карт (вырезаем значение баланса из полной строки)
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);// генерируем сумму перевода не более начального баланса первой карты
        var TransferMoneyPage = dashboardPage.selectCardToTransfer(secondCardInfo); // на странице дашборда выбираем карту, на которую переводить;
        TransferMoneyPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);// результатом работы метода будет возвращение в ЛК, поэтому пишем dashboardpage
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);//повторно после перевода даенег проверяем балансы карт
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var expectedFirstCardBalance =  firstCardBalance  - amount;
        var expectedSecondCardBalance =  secondCardBalance  + amount;
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
    @Test
    void shouldFindErrorIfAmountAboveCardBalance(){
        var loginPage = new LoginPage();// после открытия страницы объявляем новую сущность - страницу ввода логина и пароля
        var authInfo = getAuthInfo(); // получаем данные авторизации (логин и пароль) из датахелпера
        var verificationPage = loginPage.validLogin(authInfo);//вставляем данные авторизации
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // получаем код верификации и в результате открывается страница для ввода кода проверки
        var dashboardPage = verificationPage.validVerify(verificationCode);// вводим код и в результате открывается страница ЛК
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(firstCardBalance);// в данном случае для переменной var берется баланс первой карты
        var expectedFirstCardBalance =  firstCardBalance  - amount;
        var expectedSecondCardBalance =  secondCardBalance  - amount;
        var TransferMoneyPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        TransferMoneyPage.makeTransfer(String.valueOf(amount), firstCardInfo);//результатом работы НЕ будет возвраащение в ЛК. Поэтому dashboardpage не пишем
        TransferMoneyPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей баланс карты");
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }

}



