package com.limswizards.dev.clockout.repositories;

import com.limswizards.dev.clockout.models.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface Time extends JpaRepository<VacationTime, Long> {}
