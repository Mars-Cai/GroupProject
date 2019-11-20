package WorldEditor;

import java.nio.file.DirectoryIteratorException;;

public class RoomConnection {
	Room from;
	Room to;
	Square doorSquare;
	Location doorExactLoc;
	Direction direction;

	public RoomConnection(Room from, Room to, Square square,Location doorExactLoc, Direction direction) {
		this.from = from;
		this.to = to;
		this.doorSquare = square;
		this.direction = direction;
		this.doorExactLoc = doorExactLoc;
		// TODO Auto-generated constructor stub
	}
}
