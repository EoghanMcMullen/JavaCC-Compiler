import java.util.*;

class STC
{
	private Token type;
	private String dataType;
	private Token id;
	private String scope;
	private Map<String, Object> map;

	STC(Token id, Token type, String scope, String dataType)
	{
		this.id = id;
		this.type = type;
		this.scope = scope;
		this.dataType = dataType;
		this.map = new HashMap<String, Object>();
	}
	
	String type()
	{
		return dataType;
	}
	
	Object get(String key)
	{
		return map.get(key);
	}

	Token getId()
	{
		return id;
	}

	Map getData()
	{
		return map;
	}

	String getScope()
	{
		return scope;
	}

	Token getType()
	{
		return type;
	}

	void addData(String key, Object obj)
	{
		map.put(key, obj);
	}
}
