package com.koolyun.mis.util;

import java.util.ArrayList;
import java.util.List;

public class QuickMoney {
	int MoneyUnit[] = { 1, 10, 20, 50, 100 };

	double getBigTen(double money) {
		if ((int) money % 10 == 0)
			return money;
		else {
			return (int) money / 10 * 10 + 10;
		}
	}

	double getBiggerMoney(List<Double> moneyList, double money) {
		// int hundredCount = getHundredCount(money);
		double remain = getLessHundredAmount(money);
		if (remain <= 1) {
			return 1;
		}
		if (remain > 1) {
			if (checkMoreThanFive(remain)) {
				return getBigTen(remain);
			} else {
				return getBigTen(remain) - 5;
			}
		}
		return 100;

	}

	private static int getHundredCount(double money) {
		return (int) money / 100;
	}

	private static double getLessHundredAmount(double money) {
		return money - getHundredCount(money) * 100;
	}

	boolean checkMoreThanFive(double money) {
		return (int) money % 10 >= 5;
	}

	boolean addMoney(List<Double> moneyList, double money) {
		int hundredCount = getHundredCount(money);
		double remain = getLessHundredAmount(money);

		double fb = getBiggerMoney(moneyList, remain);
		if (getBiggerMoney(moneyList, remain) == 100) {
			moneyList.add(fb + hundredCount * 100);
			return false;
		}
		if (moneyList.size() > 3)
			return false;

		moneyList.add(fb + hundredCount * 100);
		return true;

	}

	List<Double> getMoneyList(double money) {
		List<Double> tmpList = new ArrayList<Double>();
		tmpList.add(money);

		while (addMoney(tmpList, money))
			;
		return tmpList;
	}

	private static double getRealValue(int hundredCount, double money) {
		return hundredCount * 100 + money;
	}

	public static List<Double> getQuickMoneyList(double money) {
		int hundredCount = getHundredCount(money);
		double remain = getLessHundredAmount(money);
		List<Double> tmpList = new ArrayList<Double>();
		if (remain == 0) {
			tmpList.add(getRealValue(hundredCount, remain));
		} else if (remain > 0 && remain <= 1) {
			if (remain != 1)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 1));
			tmpList.add(getRealValue(hundredCount, 5));
			tmpList.add(getRealValue(hundredCount, 10));
		} else if (remain > 1 && remain <= 5) {
			if (remain != 5)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 5));
			tmpList.add(getRealValue(hundredCount, 10));
			tmpList.add(getRealValue(hundredCount, 20));
		} else if (remain > 5 && remain <= 10) {
			if (remain != 10)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 10));
			tmpList.add(getRealValue(hundredCount, 20));
			tmpList.add(getRealValue(hundredCount, 50));
		} else if (remain > 10 && remain <= 15) {
			if (remain != 15)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 15));
			tmpList.add(getRealValue(hundredCount, 20));
			tmpList.add(getRealValue(hundredCount, 50));
		} else if (remain > 15 && remain <= 20) {
			if (remain != 20)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 20));
			tmpList.add(getRealValue(hundredCount, 50));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 20 && remain <= 25) {
			if (remain != 25)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 25));
			tmpList.add(getRealValue(hundredCount, 30));
			tmpList.add(getRealValue(hundredCount, 40));
		} else if (remain > 25 && remain <= 30) {
			if (remain != 30)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 30));
			tmpList.add(getRealValue(hundredCount, 40));
			tmpList.add(getRealValue(hundredCount, 50));
		} else if (remain > 30 && remain <= 35) {
			if (remain != 35)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 35));
			tmpList.add(getRealValue(hundredCount, 40));
			tmpList.add(getRealValue(hundredCount, 50));
		} else if (remain > 35 && remain <= 40) {
			if (remain != 40)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 40));
			tmpList.add(getRealValue(hundredCount, 50));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 40 && remain <= 45) {
			if (remain != 45)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 45));
			tmpList.add(getRealValue(hundredCount, 50));
			tmpList.add(getRealValue(hundredCount, 60));
		} else if (remain > 45 && remain <= 50) {
			if (remain != 50)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 50));
			tmpList.add(getRealValue(hundredCount, 60));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 50 && remain <= 55) {
			if (remain != 55)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 55));
			tmpList.add(getRealValue(hundredCount, 60));
			tmpList.add(getRealValue(hundredCount, 70));
		} else if (remain > 55 && remain <= 60) {
			if (remain != 60)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 60));
			tmpList.add(getRealValue(hundredCount, 70));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 60 && remain <= 65) {
			if (remain != 65)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 65));
			tmpList.add(getRealValue(hundredCount, 70));
			tmpList.add(getRealValue(hundredCount, 80));
		} else if (remain > 65 && remain <= 70) {
			if (remain != 70)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 70));
			tmpList.add(getRealValue(hundredCount, 80));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 70 && remain <= 75) {
			if (remain != 75)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 75));
			tmpList.add(getRealValue(hundredCount, 80));
			tmpList.add(getRealValue(hundredCount, 90));
		} else if (remain > 75 && remain <= 80) {
			if (remain != 80)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 80));
			tmpList.add(getRealValue(hundredCount, 90));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 80 && remain <= 85) {
			if (remain != 85)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 85));
			tmpList.add(getRealValue(hundredCount, 90));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 85 && remain <= 90) {
			if (remain != 90)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 90));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 90 && remain <= 95) {
			if (remain != 95)
				tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 95));
			tmpList.add(getRealValue(hundredCount, 100));
		} else if (remain > 95 && remain < 100) {
			tmpList.add(getRealValue(hundredCount, remain));
			tmpList.add(getRealValue(hundredCount, 100));
		}
		return tmpList;
	}
}
