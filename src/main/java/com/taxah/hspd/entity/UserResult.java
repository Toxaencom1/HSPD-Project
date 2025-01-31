package com.taxah.hspd.entity;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users_results")
@IdClass(UserResultId.class)
public class UserResult {

    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Result result;
}
