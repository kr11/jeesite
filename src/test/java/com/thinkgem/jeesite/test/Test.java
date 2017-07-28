package com.thinkgem.jeesite.test;

public class Test {

	public static void main(String[] args) {
		String input = "123-02";
		System.out.println(input.indexOf("-"));
		System.out.println(Integer.parseInt("123"));
		String input2 = "123-06";
		System.out.println(input2.compareTo(input));
		try {
			int a = Integer.parseInt("");
		}
		catch (Exception e){
			System.out.println("wrong");
		}

	}
}
