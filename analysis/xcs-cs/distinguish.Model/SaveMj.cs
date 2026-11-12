namespace distinguish.Model;

public class SaveMj
{
	public int mahjong_number { get; set; }

	public int serial_number { get; set; }

	public int direction { get; set; }

	public int result { get; set; }

	public SaveMj(int mahjong_number, int serial_number, int direction, int result)
	{
		this.mahjong_number = mahjong_number;
		this.serial_number = serial_number;
		this.direction = direction;
		this.result = result;
	}
}
