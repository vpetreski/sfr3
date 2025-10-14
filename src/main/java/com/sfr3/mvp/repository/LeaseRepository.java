package com.sfr3.mvp.repository;
import com.sfr3.mvp.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeaseRepository extends JpaRepository<Lease, Long> {}
