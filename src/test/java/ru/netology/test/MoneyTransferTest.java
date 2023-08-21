package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTransferTest {

    @BeforeAll
    public static void loginToPersonalAccount() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getUserAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor();
        verificationPage.validVerify(verificationCode);
    }

    @Test   //Перевод со второй карты на первую
    public void shouldTransferFromSecondToFirst() {
        //Получение баланса по обеим картам и подготовка данных для перевода денег:
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var replenishmentPage = dashboardPage.transfer(firstCardId);
        var transferInfo = DataHelper.getFirstCardTransferInfoPositive();
        //Осуществление перевода денег:
        replenishmentPage.transferBetweenOwnCards(transferInfo);
        //Получение итогового баланса по обеим картам:
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка зачисления на первую карту:
        assertEquals(transferInfo.getAmount(), finalBalanceFirstCard - initialBalanceFirstCard);
        //Проверка списания со второй карты:
        assertEquals(transferInfo.getAmount(), initialBalanceSecondCard - finalBalanceSecondCard);
    }

    @Test   //Перевод с первой карты на вторую
    public void shouldTransferFromFirstToSecond() {
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var replenishmentPage = dashboardPage.transfer(secondCardId);
        var transferInfo = DataHelper.getSecondCardTransferInfoPositive();
        //Осуществление перевода денег:
        replenishmentPage.transferBetweenOwnCards(transferInfo);
        //Получение итогового баланса по обеим картам:
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка списания с первой карты:
        assertEquals(transferInfo.getAmount(), initialBalanceFirstCard - finalBalanceFirstCard);
        //Проверка зачисления на вторую карту:
        assertEquals(transferInfo.getAmount(), finalBalanceSecondCard - initialBalanceSecondCard);
    }

    //Негативные проверки:
    @Test   //Попытка перевода со второй карты на первую с отрицательной суммой перевода
    public void shouldTransferFromSecondToFirstNegativeAmount() {
        //Получение баланса по обеим картам и подготовка данных для перевода денег:
        var dashboardPage = new DashboardPage();
        var firstCardId = DataHelper.getFirstCardId();
        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var secondCardId = DataHelper.getSecondCardId();
        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        var replenishmentPage = dashboardPage.transfer(firstCardId);
        var transferInfo = DataHelper.getFirstCardTransferInfoNegative();
        //Осуществление перевода денег:
        replenishmentPage.transferBetweenOwnCards(transferInfo);
        //Получение итогового баланса по обеим картам:
        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        //Проверка зачисления на первую карту:
        assertEquals( - transferInfo.getAmount(), finalBalanceFirstCard - initialBalanceFirstCard);
        //Проверка списания со второй карты:
        assertEquals( - transferInfo.getAmount(), initialBalanceSecondCard - finalBalanceSecondCard);
    }

//    @Test   //Попытка перевода с первой карты на вторую с суммой перевода превышающей баланс первой карты
//    public void shouldTransferFromFirstToSecondNegativeAmount() {
//        //Получение баланса по обеим картам и подготовка данных для перевода денег:
//        var dashboardPage = new DashboardPage();
//        var firstCardId = DataHelper.getFirstCardId();
//        var initialBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
//        var secondCardId = DataHelper.getSecondCardId();
//        var initialBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
//        var replenishmentPage = dashboardPage.transfer(secondCardId);
//        var transferInfo = DataHelper.getSecondCardTransferInfoNegative();
//        //Попытка осуществление перевода денег:
//        replenishmentPage.transferBetweenOwnCards(transferInfo);
//        //Получение итогового баланса по обеим картам:
//        var finalBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
//        var finalBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
//        //Проверка на изменение баланса первой карты:
//        assertEquals(initialBalanceFirstCard, finalBalanceFirstCard);
//        //Проверка на изменение баланса второй карты:
//        assertEquals(initialBalanceSecondCard, finalBalanceSecondCard);
//
//    }
}