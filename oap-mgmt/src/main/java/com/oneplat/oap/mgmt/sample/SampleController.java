package com.oneplat.oap.mgmt.sample;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hyung Joo Lee
 *
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

	@RequestMapping(value = "/sample1", method = RequestMethod.POST)
	public @ResponseBody String sample1(@RequestBody SampleVo requestVo) {
		System.out.println("POST Request received.");
		System.out.println(requestVo);
		return "success";
	}

	@RequestMapping(value = "/sample1/{userId}", method = RequestMethod.PUT)
	public @ResponseBody String sample1put(@PathVariable Integer userId, @RequestBody SampleVo requestVo) {
		System.out.println("PUT Request received.");
		System.out.println(requestVo);
		return "success";
	}

	@RequestMapping(value = "/sample2/{userId}", method = RequestMethod.GET)
	public @ResponseBody SampleVo sample2(@PathVariable Integer userId) {
		return userId == 1 ? SampleVo.getSampleVo1() : SampleVo.getSampleVo2();
	}

	@RequestMapping(value = "/sample2", method = RequestMethod.GET)
	public @ResponseBody SampleVo sample22(@RequestParam(name = "userId") Integer userId,
			@RequestParam(name = "info") Boolean info) {
		return (info != null && info) ? userId == 1 ? SampleVo.getSampleVo1WithInfo() : SampleVo.getSampleVo2WithInfo()
				: userId == 1 ? SampleVo.getSampleVo1() : SampleVo.getSampleVo2();

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SampleVo {

		private Integer id;
		private String name;
		private Integer age;
		private String email;
		private String phone;

		public SampleVo(Integer id, String name, Integer age) {
			super();
			this.id = id;
			this.name = name;
			this.age = age;
		}

		public static SampleVo getSampleVo1WithInfo() {
			return new SampleVo(1, "류재영", 36, "longbe@oneplat.co", "010-1234-4321");
		}

		public static SampleVo getSampleVo2WithInfo() {
			return new SampleVo(2, "이희태", 34, "ht.lee@oneplat.co", "010-1234-5678");
		}

		public static SampleVo getSampleVo1() {
			return new SampleVo(1, "류재영", 36);
		}

		public static SampleVo getSampleVo2() {
			return new SampleVo(2, "이희태", 34);
		}

	}

}
