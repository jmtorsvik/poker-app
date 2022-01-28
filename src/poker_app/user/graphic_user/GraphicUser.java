package poker_app.user.graphic_user;

import poker_app.game.Player;
import poker_app.user.User;

public class GraphicUser extends User {

	public GraphicUser(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAction(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean rebuy(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sitOutNextHand(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int pickSeat(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseStartingStack(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
