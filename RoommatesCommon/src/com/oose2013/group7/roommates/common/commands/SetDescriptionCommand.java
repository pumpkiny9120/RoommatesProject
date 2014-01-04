package com.oose2013.group7.roommates.common.commands;

import com.oose2013.group7.roommates.common.interfaces.Command;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameAbstract;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameProxy;

/***Class representing a command object that is sent to server to set
 * a description for current query**/
public class SetDescriptionCommand implements Command {

	public SetDescriptionCommand(String text, String userName) {
		this.text = text;
		this.userName=userName;
	}

	private String text;
	private String userName;
	public String getText() {
		return text;
	}
	public String getUser(){
		return userName;
	}

	@Override
	public void execute(Context context) {
		DescribeGameProxy describeGame =  (DescribeGameProxy) context.getGame();
		describeGame.setDescription(text,userName);
	}

}
