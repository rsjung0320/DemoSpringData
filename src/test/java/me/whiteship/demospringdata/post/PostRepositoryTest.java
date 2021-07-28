package me.whiteship.demospringdata.post;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PostRepositoryTestConfig.class)
public class PostRepositoryTest {

	@Autowired
	PostRepository postRepository;

	@Test
	public void crud() {
		Post post = new Post();
		post.setTitle("hibernate");

		assertThat(postRepository.contains(post)).isFalse();

		// save 할 때 event 발생함
		postRepository.save(post.publish());

		assertThat(postRepository.contains(post)).isTrue();

		// 테스트라 다시 롤백이 될 것이라고 알고 있기 때문에 flush()가 없이는 아래 delete는 실행이 안될 것이다.
		postRepository.delete(post);
		postRepository.flush();
	}

	@Test
	public void queryDsl() {
		Post post = new Post();
		post.setTitle("hibernate");
		postRepository.save(post.publish());

		Predicate predicate = QPost.post
			.title.containsIgnoreCase("Hi");

		Optional<Post> one = postRepository.findOne(predicate);
		assertThat(one).isNotEmpty();

	}

}