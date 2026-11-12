using System;
using System.Text;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.Pkcs;
using Org.BouncyCastle.Asn1.X509;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Encodings;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Generators;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Math;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.X509;

namespace distinguish.Common;

internal class CryptionUtils
{
	public struct RSAKEY
	{
		public string PublicKey { get; set; }

		public string PrivateKey { get; set; }
	}

	public static RSAKEY GetKey()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Expected O, but got Unknown
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Expected O, but got Unknown
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		RsaKeyPairGenerator val = new RsaKeyPairGenerator();
		RsaKeyGenerationParameters val2 = new RsaKeyGenerationParameters(BigInteger.ValueOf(3), new SecureRandom(), 2048, 25);
		val.Init((KeyGenerationParameters)(object)val2);
		AsymmetricCipherKeyPair obj = val.GenerateKeyPair();
		AsymmetricKeyParameter val3 = obj.Public;
		AsymmetricKeyParameter val4 = obj.Private;
		SubjectPublicKeyInfo obj2 = SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(val3);
		PrivateKeyInfo val5 = PrivateKeyInfoFactory.CreatePrivateKeyInfo(val4);
		byte[] encoded = ((Asn1Encodable)((Asn1Encodable)obj2).ToAsn1Object()).GetEncoded("UTF-8");
		byte[] encoded2 = ((Asn1Encodable)((Asn1Encodable)val5).ToAsn1Object()).GetEncoded("UTF-8");
		return new RSAKEY
		{
			PublicKey = Convert.ToBase64String(encoded),
			PrivateKey = Convert.ToBase64String(encoded2)
		};
	}

	private static AsymmetricKeyParameter GetPublicKeyParameter(string s)
	{
		s = s.Replace("\r", "").Replace("\n", "").Replace(" ", "");
		byte[] array = Convert.FromBase64String(s);
		Asn1Object.FromByteArray(array);
		return PublicKeyFactory.CreateKey(array);
	}

	private static AsymmetricKeyParameter GetPrivateKeyParameter(string s)
	{
		s = s.Replace("\r", "").Replace("\n", "").Replace(" ", "");
		return PrivateKeyFactory.CreateKey(Convert.FromBase64String(s));
	}

	public static string EncryptByPrivateKey(string s, string key)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_000a: Expected O, but got Unknown
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		IAsymmetricBlockCipher val = (IAsymmetricBlockCipher)new Pkcs1Encoding((IAsymmetricBlockCipher)new RsaEngine());
		try
		{
			val.Init(true, (ICipherParameters)(object)GetPrivateKeyParameter(key));
			byte[] bytes = Encoding.UTF8.GetBytes(s);
			return Convert.ToBase64String(val.ProcessBlock(bytes, 0, bytes.Length));
		}
		catch (Exception ex)
		{
			return ex.Message;
		}
	}

	public static string EncryptByPublicKey(string s, string key)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_000a: Expected O, but got Unknown
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		IAsymmetricBlockCipher val = (IAsymmetricBlockCipher)new Pkcs1Encoding((IAsymmetricBlockCipher)new RsaEngine());
		try
		{
			val.Init(true, (ICipherParameters)(object)GetPublicKeyParameter(key));
			byte[] bytes = Encoding.UTF8.GetBytes(s);
			return Convert.ToBase64String(val.ProcessBlock(bytes, 0, bytes.Length));
		}
		catch (Exception ex)
		{
			return ex.Message;
		}
	}

	public static string DecryptByPublicKey(string s, string key)
	{
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected O, but got Unknown
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Expected O, but got Unknown
		s = s.Replace("\r", "").Replace("\n", "").Replace(" ", "");
		IAsymmetricBlockCipher val = (IAsymmetricBlockCipher)new Pkcs1Encoding((IAsymmetricBlockCipher)new RsaEngine());
		try
		{
			val.Init(false, (ICipherParameters)(object)GetPublicKeyParameter(key));
			byte[] array = Convert.FromBase64String(s);
			byte[] bytes = val.ProcessBlock(array, 0, array.Length);
			return Encoding.UTF8.GetString(bytes);
		}
		catch (Exception ex)
		{
			return ex.Message;
		}
	}

	public static string DecryptByPrivateKey(string s, string key)
	{
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected O, but got Unknown
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Expected O, but got Unknown
		s = s.Replace("\r", "").Replace("\n", "").Replace(" ", "");
		IAsymmetricBlockCipher val = (IAsymmetricBlockCipher)new Pkcs1Encoding((IAsymmetricBlockCipher)new RsaEngine());
		try
		{
			val.Init(false, (ICipherParameters)(object)GetPrivateKeyParameter(key));
			byte[] array = Convert.FromBase64String(s);
			byte[] bytes = val.ProcessBlock(array, 0, array.Length);
			return Encoding.UTF8.GetString(bytes);
		}
		catch (Exception ex)
		{
			return ex.Message;
		}
	}
}
