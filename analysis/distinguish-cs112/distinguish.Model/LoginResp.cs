using System.Collections.Generic;

namespace distinguish.Model;

public class LoginResp
{
	private LoginUser loginUser;

	private List<Device> devices;

	private bool success;

	private string jwt;

	public LoginUser LoginUser
	{
		get
		{
			return loginUser;
		}
		set
		{
			loginUser = value;
		}
	}

	public List<Device> Devices
	{
		get
		{
			return devices;
		}
		set
		{
			devices = value;
		}
	}

	public bool Success
	{
		get
		{
			return success;
		}
		set
		{
			success = value;
		}
	}

	public string Jwt
	{
		get
		{
			return jwt;
		}
		set
		{
			jwt = value;
		}
	}
}
