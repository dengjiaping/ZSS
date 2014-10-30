package com.koolyun.mis.line;

import java.util.List;

import com.koolyun.mis.core.user.Account;
import com.koolyun.mis.util.communicate.OperateTimer;

public abstract class Line {

	abstract public void pollTimerJob(List<OperateTimer> ot);

	abstract public void orderTimerJob();

	abstract public void productTimerJob();

	abstract public int loginProceess(Account mAccount);
}
