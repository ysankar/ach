package com.bfds.ach.test;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bfds.ach.domain.reference.SystemName;
import com.bfds.ach.exe.domain.DemandDepositAccount;
import com.bfds.ach.exe.domain.Fund;
import com.bfds.ach.exe.repositories.DemandDepositAccountRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/ach-repository-jpa-test.xml")
public class DemandDepositAccountTest {

	@Autowired
	DemandDepositAccountRepository demandDepositAccountRepository; 

	/**
	 * Create a demand deposit account and verify the same
	 */
	@Test
	public void createAndVerifyDemandDepositAccount() {

		DemandDepositAccount dda = new DemandDepositAccount();
		Integer mgmtCompNoOrDda = 100;

		if(mgmtCompNoOrDda<1000){
			dda.setMgmtCompanyNo(mgmtCompNoOrDda);
		}else{
			dda.setDda1(mgmtCompNoOrDda);
		}
		dda.setMgmtCode("M1");
		dda.setMgmtCodeName("Management Code 1");
		dda.setSystemName(SystemName.AMV);

		Set<Fund> fundsSet = new HashSet<Fund>();

		Fund fund1 = new Fund();
		fund1.setFundNo(1);
		fund1.setFundName("Fund 1");
		fund1.setDda(dda);

		Fund fund2 = new Fund();
		fund2.setFundNo(2);
		fund2.setFundName("Fund 2");
		fund2.setDda(dda);

		fundsSet.add(fund1);
		fundsSet.add(fund2);
		dda.setFund(fundsSet);

		demandDepositAccountRepository.saveAndFlush(dda); //insert	

		DemandDepositAccount ddaRec = mgmtCompNoOrDda<1000?
				demandDepositAccountRepository.findByMgmtCompanyNo(mgmtCompNoOrDda):
				demandDepositAccountRepository.findBydda1(mgmtCompNoOrDda);

		assertThat(ddaRec.getMgmtCompanyNo()).isEqualTo(100);
		assertNull("DDA number is null",ddaRec.getDda1());
		assertThat(ddaRec.getFund()).isNotNull();
		assertThat(ddaRec.getFund().size()).isEqualTo(2);
	}
	

}
