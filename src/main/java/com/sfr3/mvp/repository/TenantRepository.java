package com.sfr3.mvp.repository;
import com.sfr3.mvp.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TenantRepository extends JpaRepository<Tenant, Long> {}
