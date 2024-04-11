package com.project.AzCar.Entities.Reply;

import java.io.Serializable;

import com.project.AzCar.Entities.Comments.Comments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_replies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reply implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comments comment_id;

	private String content;

	private long user_id;

}
