using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Windows;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using WebSocketSharp;

namespace distinguish.Common;

public class HttpReq
{
	public static string post(string pathBase, string jsonData)
	{
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Expected Obj, but got Unknown
		//IL_00f6: Unknown result type (might be due to invalid IL or missing references)
		HttpClient httpClient = new HttpClient();
		string text = "http://" + Global.ipAddr + ":8081/";
		if (!pathBase.StartsWith("account/edit/pwd"))
		{
			text += "api/";
		}
		Uri requestUri = new Uri(text + pathBase);
		httpClient.DefaultRequestHeaders.Add("Authorization", "Bearer " + Global.jwt);
		HttpContent httpContent = new StringContent(jsonData);
		httpContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
		string result = httpClient.PostAsync(requestUri, httpContent).Result.Content.ReadAsStringAsync().Result;
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(result);
		if (!(bool)dictionary["success"] && int.Parse(dictionary["code"].ToString()) == 401)
		{
			MessageBox.Show("当前登录已失效,请重新登录!");
			return "";
		}
		return result;
	}

	public static string get(string urlPath)
	{
		//IL_0086: Unknown result type (might be due to invalid IL or missing references)
		//IL_008b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0091: Unknown result type (might be due to invalid IL or missing references)
		//IL_009b: Expected Obj, but got Unknown
		//IL_00db: Unknown result type (might be due to invalid IL or missing references)
		HttpClient httpClient = new HttpClient
		{
			DefaultRequestHeaders = { 
			{
				"Authorization",
				"Bearer " + Global.jwt
			} }
		};
		Uri requestUri = new Uri("http://" + Global.ipAddr + ":8081/api/" + urlPath);
		new StringContent("{}").Headers.ContentType = new MediaTypeHeaderValue("application/json");
		string result = httpClient.GetAsync(requestUri).Result.Content.ReadAsStringAsync().Result;
		if (!Ext.IsNullOrEmpty(result))
		{
			Dictionary<string, object> dictionary = new Dictionary<string, object>();
			JsonSerializer val = new JsonSerializer();
			((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
			val.NullValueHandling = (NullValueHandling)1;
			dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(result);
			if (!(bool)dictionary["success"] && int.Parse(dictionary["code"].ToString()) == 401)
			{
				MessageBox.Show("当前登录已失效,请重新登录!");
				return "";
			}
			return result;
		}
		return "";
	}
}
