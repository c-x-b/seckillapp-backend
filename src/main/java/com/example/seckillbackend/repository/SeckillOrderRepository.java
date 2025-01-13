package com.example.seckillbackend.repository;

import com.example.seckillbackend.entity.SeckillOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SeckillOrderRepository extends JpaRepository<SeckillOrder, Long> {
}
