package com.sflow.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FlowFilterE {

	/* l2 */
	DMAC("dmac", DataTypeE.UINT8, 6),
	SMAC("smac", DataTypeE.UINT8, 6),
	ETYPE("etype", DataTypeE.UINT16, 1),
	VLAN("vlan", DataTypeE.UINT16, 1),
	QINQ("qinq", DataTypeE.UINT16, 1),
	L2_FLAGS("l2_flags", DataTypeE.UINT16, 1),
	
	/* l3 */
    /* IP v6 */
	SIP6("sip6", DataTypeE.UINT8, 16),
	DIP6("dip6", DataTypeE.UINT8, 16),
	TRAFFIC_CLASS("traffic_class", DataTypeE.UINT8, 1 ),
	NEXT_HEADER("next_header", DataTypeE.UINT8, 1),
	FLOW_LABEL("flow_label", DataTypeE.UINT32, 1),

	/* IP v4 */
	SIP("sip", DataTypeE.UINT32, 1),
	DIP("dip", DataTypeE.UINT32, 1),
	PROTO("proto", DataTypeE.UINT8, 1),
	L3_FLAGS("l3_flags", DataTypeE.UINT8, 1),
	
    /* l4 */
	SP("sp", DataTypeE.UINT16, 1),
	DP("dp", DataTypeE.UINT16, 1),
	ICMP_TYPE("icmp_type", DataTypeE.UINT8, 1 ),
	ICMP_CODE("icmp_code", DataTypeE.UINT8, 1),
	TCP_FLAGS("tcp_flags", DataTypeE.UINT8, 1),
	
    /* Tunneling header */
	SPII("spii", DataTypeE.UINT32, 1); /* NSH Service Path ID and Index */

	private String     name;
	private int        len;
	private DataTypeE  type;
	
	private static final FlowFilterE[] vals = values();

	private FlowFilterE(String name, DataTypeE type, int n) {
		this.name = name;
		this.setType(type);
		this.setLen(n);
	}

	public String getName() {
		return name;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public DataTypeE getType() {
		return type;
	}

	public void setType(DataTypeE type) {
		this.type = type;
	}
	
	public FlowFilterE next(){
         return vals[(this.ordinal()+1) % vals.length];
    }
	
	public FlowFilterE prev() {
		return vals[(this.ordinal()-1) % vals.length];	
	}

	public int offset() {
		if (this.ordinal() == 0) {
			return 0;
		}
		return (this.prev().getLen() * this.prev().getType().nbytes()) + this.prev().offset();
	}
		
	@JsonCreator
	public static FlowFilterE fromString(String text) {
		if (text != null) {
			for (FlowFilterE b : FlowFilterE.values()) {
				if (text.equalsIgnoreCase(b.getName())) {					
					return b;
				}
			}
		}
	
		throw new IllegalArgumentException();
	}

	public String toString() {
		return getName();
	}

	public FlowFilterE convert(String arg0) {
		return fromString(arg0);
	}
}