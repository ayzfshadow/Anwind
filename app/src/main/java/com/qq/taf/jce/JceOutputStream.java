package com.qq.taf.jce;

//
// Decompiled by CFR - 1940ms
//

//
// Decompiled by Jadx - 1052ms
//

//
// Decompiled by FernFlower - 1629ms
//

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JceOutputStream {
	private ByteBuffer bs;
	protected String sServerEncoding;

	public JceOutputStream() {
		this(128);
	}

	public JceOutputStream(int var1) {
		this.sServerEncoding = "GBK";
		this.bs = ByteBuffer.allocate(var1);
	}

	public JceOutputStream(ByteBuffer var1) {
		this.sServerEncoding = "GBK";
		this.bs = var1;
	}

	public static void main(String[] var0) {
		JceOutputStream var2 = new JceOutputStream();
		var2.write(1311768467283714885L, 0);
		ByteBuffer var1 = var2.getByteBuffer();
		System.out.println(HexUtil.bytes2HexStr(var1.array()));
		System.out.println(Arrays.toString(var2.toByteArray()));
	}

	private void writeArray(Object[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write((Object)var1[var2], 0);
		}

	}

	public ByteBuffer getByteBuffer() {
		return this.bs;
	}



	public void reserve(int var1) {
		if (this.bs.remaining() < var1) {
			int var2 = (this.bs.capacity() + var1) * 2;

			try {
				ByteBuffer var6 = ByteBuffer.allocate(var2);
				var6.put(this.bs.array(), 0, this.bs.position());
				this.bs = var6;
			} catch (IllegalArgumentException var5) {

				throw var5;
			}
		}
	}


	public int setServerEncoding(String var1) {
		this.sServerEncoding = var1;
		return 0;
	}

	public byte[] toByteArray() {
		byte[] var1 = new byte[this.bs.position()];
		System.arraycopy(this.bs.array(), 0, var1, 0, this.bs.position());
		return var1;
	}

	public void write(byte var1, int var2) {
		this.reserve(3);
		if (var1 == 0) {
			this.writeHead((byte)12, var2);
		} else {
			this.writeHead((byte)0, var2);
			this.bs.put(var1);
		}
	}

	public void write(double var1, int var3) {
		this.reserve(10);
		this.writeHead((byte)5, var3);
		this.bs.putDouble(var1);
	}

	public void write(float var1, int var2) {
		this.reserve(6);
		this.writeHead((byte)4, var2);
		this.bs.putFloat(var1);
	}

	public void write(int var1, int var2) {
		this.reserve(6);
		if (var1 >= -32768 && var1 <= 32767) {
			this.write((short)var1, var2);
		} else {
			this.writeHead((byte)2, var2);
			this.bs.putInt(var1);
		}
	}

	public void write(long var1, int var3) {
		this.reserve(10);
		if (var1 >= -2147483648L && var1 <= 2147483647L) {
			this.write((int)var1, var3);
		} else {
			this.writeHead((byte)3, var3);
			this.bs.putLong(var1);
		}
	}

	public void write(JceStruct var1, int var2) {
		this.reserve(2);
		this.writeHead((byte)10, var2);
		var1.writeTo(this);
		this.reserve(2);
		this.writeHead((byte)11, 0);
	}

	public void write(Boolean var1, int var2) {
		this.write(var1.booleanValue(), var2);
	}

	public void write(Byte var1, int var2) {
		this.write(var1.byteValue(), var2);
	}

	public void write(Double var1, int var2) {
		this.write(var1.doubleValue(), var2);
	}

	public void write(Float var1, int var2) {
		this.write(var1.floatValue(), var2);
	}

	public void write(Integer var1, int var2) {
		this.write(var1.intValue(), var2);
	}

	public void write(Long var1, int var2) {
		this.write(var1.longValue(), var2);
	}

	public void write(Object var1, int var2) {
		if (var1 instanceof Byte) {
			this.write(((Byte)var1).byteValue(), var2);
		} else if (var1 instanceof Boolean) {
			this.write(((Boolean)var1).booleanValue(), var2);
		} else if (var1 instanceof Short) {
			this.write(((Short)var1).shortValue(), var2);
		} else if (var1 instanceof Integer) {
			this.write(((Integer)var1).intValue(), var2);
		} else if (var1 instanceof Long) {
			this.write(((Long)var1).longValue(), var2);
		} else if (var1 instanceof Float) {
			this.write(((Float)var1).floatValue(), var2);
		} else if (var1 instanceof Double) {
			this.write(((Double)var1).doubleValue(), var2);
		} else if (var1 instanceof String) {
			this.write((String)var1, var2);
		} else if (var1 instanceof Map) {
			this.write((Map)var1, var2);
		} else if (var1 instanceof List) {
			this.write((Collection)((List)var1), var2);
		} else if (var1 instanceof JceStruct) {
			this.write((JceStruct)var1, var2);
		} else if (var1 instanceof byte[]) {
			this.write((byte[])var1, var2);
		} else if (var1 instanceof boolean[]) {
			this.write((boolean[])var1, var2);
		} else if (var1 instanceof short[]) {
			this.write((short[])var1, var2);
		} else if (var1 instanceof int[]) {
			this.write((int[])var1, var2);
		} else if (var1 instanceof long[]) {
			this.write((long[])var1, var2);
		} else if (var1 instanceof float[]) {
			this.write((float[])var1, var2);
		} else if (var1 instanceof double[]) {
			this.write((double[])var1, var2);
		} else if (var1.getClass().isArray()) {
			this.writeArray((Object[])var1, var2);
		} else if (var1 instanceof Collection) {
			this.write((Collection)var1, var2);
		} else {
			StringBuilder var3 = new StringBuilder();
			var3.append("write object error: unsupport type. ");
			var3.append(var1.getClass());
			throw new JceEncodeException(var3.toString());
		}
	}

	public void write(Short var1, int var2) {
		this.write(var1.shortValue(), var2);
	}

	public void write(String var1, int var2) {
		try {
			byte[] var3 = var1.getBytes(this.sServerEncoding);
			this.reserve(var3.length + 10);
			if (var3.length > 255) {
				this.writeHead((byte)7, var2);
				this.bs.putInt(var3.length);
				this.bs.put(var3);
			}
		} catch (UnsupportedEncodingException var4) {
			;
		}

		byte[] var5 = var1.getBytes();
		this.writeHead((byte)6, var2);
		this.bs.put((byte)var5.length);
		this.bs.put(var5);
	}

	public void write(Collection var1, int var2) {
		if (var1 != null) {
			this.reserve(8);
			this.writeHead((byte)9, var2);
			this.write((int)var1.size(), 0);
			Iterator var3 = var1.iterator();

			while(var3.hasNext()) {
				this.write((Object)var3.next(), 0);
			}

		}
	}

	public void write(Map var1, int var2) {
		if (var1 != null) {
			this.reserve(8);
			this.writeHead((byte)8, var2);
			this.write((int)var1.size(), 0);
			Iterator var3 = var1.entrySet().iterator();

			while(var3.hasNext()) {
				Entry var4 = (Entry)var3.next();
				this.write((Object)var4.getKey(), 0);
				this.write((Object)var4.getValue(), 1);
			}

		}
	}

	public void write(short var1, int var2) {
		this.reserve(4);
		if (var1 >= -128 && var1 <= 127) {
			this.write((byte)var1, var2);
		} else {
			this.writeHead((byte)1, var2);
			this.bs.putShort(var1);
		}
	}


	public void write(byte[] var1, int var2) {
		this.reserve(var1.length + 8);
		this.writeHead((byte)13, var2);
		this.writeHead((byte)0, 0);
		this.write((int)var1.length, 0);
		this.bs.put(var1);
	}

	public void write(double[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write(var1[var2], 0);
		}

	}

	public void write(float[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write(var1[var2], 0);
		}

	}

	public void write(int[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write((int)var1[var2], 0);
		}

	}

	public void write(long[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write(var1[var2], 0);
		}

	}

	public void write(Object[] var1, int var2) {
		this.writeArray(var1, var2);
	}

	public void write(short[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write((short)var1[var2], 0);
		}

	}

	public void write(boolean[] var1, int var2) {
		this.reserve(8);
		this.writeHead((byte)9, var2);
		this.write((int)var1.length, 0);
		int var3 = var1.length;

		for(var2 = 0; var2 < var3; ++var2) {
			this.write(var1[var2], 0);
		}

	}

	public void writeByteString(String var1, int var2) {
		this.reserve(var1.length() + 10);
		byte[] var3 = HexUtil.hexStr2Bytes(var1);
		if (var3.length > 255) {
			this.writeHead((byte)7, var2);
			this.bs.putInt(var3.length);
			this.bs.put(var3);
		} else {
			this.writeHead((byte)6, var2);
			this.bs.put((byte)var3.length);
			this.bs.put(var3);
		}
	}

	public void writeHead(byte var1, int var2) {
		byte var3;
		if (var2 < 15) {
			var3 = (byte)(var1 | var2 << 4);
			this.bs.put(var3);
		} else if (var2 < 256) {
			var3 = (byte)(var1 | 240);
			this.bs.put(var3);
			this.bs.put((byte)var2);
		} else {
			StringBuilder var4 = new StringBuilder();
			var4.append("tag is too large: ");
			var4.append(var2);
			throw new JceEncodeException(var4.toString());
		}
	}

	public void writeStringByte(String var1, int var2) {
		byte[] var3 = HexUtil.hexStr2Bytes(var1);
		this.reserve(var3.length + 10);
		if (var3.length > 255) {
			this.writeHead((byte)7, var2);
			this.bs.putInt(var3.length);
			this.bs.put(var3);
		} else {
			this.writeHead((byte)6, var2);
			this.bs.put((byte)var3.length);
			this.bs.put(var3);
		}
	}
}

