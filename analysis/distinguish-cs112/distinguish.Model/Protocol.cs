namespace distinguish.Model;

internal class Protocol
{
	public int types { get; set; }

	public string data_content { get; set; }

	public string from { get; set; }

	public string to { get; set; }

	public string fp { get; set; }

	public bool qos { get; set; }

	public int retry_count { get; set; }
}
