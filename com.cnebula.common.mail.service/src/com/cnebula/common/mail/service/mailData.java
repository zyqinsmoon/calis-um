package com.cnebula.common.mail.service;

public class mailData {
	private String head;
	   private String tail;
	   private Object content;
	   public void setHead(String head)
	   {
		   this.head=head;
	   }
	   public String getHead(){
		   return head;
	   }
	   public String getTail(){
		   return tail;
	   }
	   public void setTail(String tail){
		   this.tail=tail;
	   }
	   public void setContent(Object content){
		   this.content=content;
	   }
	   public Object getContent(){
		   return content;
	   }
}
