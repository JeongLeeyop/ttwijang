package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "settlement")
public class Settlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    @Column(nullable = false)
    private String teamUid;

    private String teamName;

    private String ownerUid;

    private String bankName;

    private String bankAccount;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private Integer itemCount;

    @Column(nullable = false, length = 7)
    private String period; // "YYYY-MM"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementStatus status;

    private LocalDateTime settledAt;

    @Column(length = 500)
    private String adminNote;

    @CreationTimestamp
    private LocalDateTime createdDate;

    public enum SettlementStatus {
        PENDING, COMPLETED
    }
}
