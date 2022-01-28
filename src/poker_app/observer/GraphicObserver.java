package poker_app.observer;

import java.util.List;

import poker_app.Table;
import poker_app.game.Player;

public class GraphicObserver extends TableObserver {

	public GraphicObserver(Table table) {
		super(table);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startObserving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopObserving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newStreet(char street) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fold(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void call(Player player, int callTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bet(Player player, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void raise(Player player, int raiseTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void win(Player player, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revealHands(List<Player> players) {
		// TODO Auto-generated method stub
		
	}

}
