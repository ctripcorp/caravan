package com.ctrip.soa.caravan.util.serializer;

import com.ctrip.soa.caravan.util.serializer.ssjson.TypeAlias;
import com.ctrip.soa.caravan.util.serializer.ssjson.TypeAliasIdResolver;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@JsonPropertyOrder({ "item", "orderName", "hotelType" })
public class ObjectFieldPojo {

	public static final ObjectFieldPojo INSTANCE;

	static {
		TypeAliasIdResolver.registerType(CityIDType.class);
		TypeAliasIdResolver.registerType(HotelIDType.class);
		TypeAliasIdResolver.registerType(OrderIDType.class);

		ObjectFieldPojo instance = new ObjectFieldPojo();
		instance.setHotelType(3);
		instance.setOrderName("orderNameTest");

		CityIDType item = new CityIDType();
		item.setCityID(90043);
		instance.setItem(item);

		INSTANCE = instance;
	}

	@JsonTypeInfo(use = Id.CUSTOM, include = As.PROPERTY, property = "__type")
	@JsonTypeIdResolver(TypeAliasIdResolver.class)
	@JsonProperty("Item")
	protected Object item;

	@JsonProperty("OrderName")
	protected String orderName;
	@JsonProperty("HotelType")
	protected long hotelType;

	/**
	 * 获取item属性的值。
	 * 
	 * @return possible object is {@link OrderIDType } {@link HotelIDType }
	 *         {@link CityIDType }
	 * 
	 */
	public Object getItem() {
		return item;
	}

	/**
	 * 设置item属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link OrderIDType } {@link HotelIDType }
	 *            {@link CityIDType }
	 * 
	 */
	public void setItem(Object item) {
		this.item = item;
	}

	/**
	 * 获取orderName属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderName() {
		return orderName;
	}

	/**
	 * 设置orderName属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderName(String value) {
		this.orderName = value;
	}

	/**
	 * 获取hotelType属性的值。
	 * 
	 */
	public long getHotelType() {
		return hotelType;
	}

	/**
	 * 设置hotelType属性的值。
	 * 
	 */
	public void setHotelType(long value) {
		this.hotelType = value;
	}

	@TypeAlias("TestServices.CityIDType")
	public static class CityIDType {
		@JsonProperty("cityID")
		protected long cityID;

		/**
		 * 获取cityID属性的值。
		 * 
		 */
		public long getCityID() {
			return cityID;
		}

		/**
		 * 设置cityID属性的值。
		 * 
		 */
		public void setCityID(long value) {
			this.cityID = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (cityID ^ (cityID >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CityIDType other = (CityIDType) obj;
			if (cityID != other.cityID)
				return false;
			return true;
		}

	}

	@TypeAlias("TestServices.HotelIDType")
	public static class HotelIDType {
		@JsonProperty("hotelID")
		protected String hotelID;

		/**
		 * 获取hotelID属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getHotelID() {
			return hotelID;
		}

		/**
		 * 设置hotelID属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setHotelID(String value) {
			this.hotelID = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((hotelID == null) ? 0 : hotelID.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			HotelIDType other = (HotelIDType) obj;
			if (hotelID == null) {
				if (other.hotelID != null)
					return false;
			} else if (!hotelID.equals(other.hotelID))
				return false;
			return true;
		}

	}

	@TypeAlias("TestServices.OrderIDType")
	public static class OrderIDType {
		@JsonProperty("orderID")
		protected int orderID;

		/**
		 * 获取orderID属性的值。
		 * 
		 */
		public int getOrderID() {
			return orderID;
		}

		/**
		 * 设置orderID属性的值。
		 * 
		 */
		public void setOrderID(int value) {
			this.orderID = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + orderID;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OrderIDType other = (OrderIDType) obj;
			if (orderID != other.orderID)
				return false;
			return true;
		}

	}

}
