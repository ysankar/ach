package com.bfds.ach.exe.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bfds.ach.exe.domain.DemandDepositAccount;

public interface DemandDepositAccountRepository extends JpaRepository<DemandDepositAccount, Long> {
	
	DemandDepositAccount findByMgmtCompanyNo(Integer mgmtCompanyNo);
	DemandDepositAccount findBydda1(Integer dda1);
}