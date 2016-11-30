/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.rooms.IHaveSpecialEvent;

/**
 * @author Caoda
 */
public class TimeItem extends Item implements IHaveSpecialEvent {

	public TimeItem() {
		super(ItemType.SPECIAL);
	}

	@Override
	public String doSpecialEvent(Game game) {
		//game.extendGameTime(60);
		game.extendGameTime(60);
		return "IKEA will be closing one hour later than scheduled! :)";
	}
}

