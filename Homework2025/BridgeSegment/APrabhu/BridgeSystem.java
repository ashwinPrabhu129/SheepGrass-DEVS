package Homework2025.BridgeSegment.APrabhu;

import BridgeSegment.AbstractBridgeSystem;

public class BridgeSystem extends AbstractBridgeSystem {

    public BridgeSystem(String name) {
        super(name);

        BridgeSegment Seg1 = new BridgeSegment("Seg1", 10.0);
        BridgeSegment Seg2 = new BridgeSegment("Seg2", 10.0);
        BridgeSegment Seg3 = new BridgeSegment("Seg3", 10.0);

        add(Seg1);
        add(Seg2);
        add(Seg3);

        add(this.westCarGenerator);
        add(this.eastCarGenerator);
        add(this.transduser);

        addCoupling(this.westCarGenerator, "out", Seg3, "westbound_in");
        addCoupling(this.eastCarGenerator, "out", Seg1, "eastbound_in");

        addCoupling(Seg1, "westbound_out", Seg2, "westbound_in");
        addCoupling(Seg2, "westbound_out", Seg3, "westbound_in");
        addCoupling(Seg3, "eastbound_out", Seg2, "eastbound_in");
        addCoupling(Seg2, "eastbound_out", Seg1, "eastbound_in");


        addCoupling(Seg1, "eastbound_out", this.transduser, "Bridge1_EastOut");
        addCoupling(Seg1, "westbound_out", this.transduser, "Bridge1_WestOut");
        addCoupling(Seg2, "eastbound_out", this.transduser, "Bridge2_EastOut");
        addCoupling(Seg2, "westbound_out", this.transduser, "Bridge2_WestOut");
        addCoupling(Seg3, "eastbound_out", this.transduser, "Bridge3_EastOut");
        addCoupling(Seg3, "westbound_out", this.transduser, "Bridge3_WestOut");
    }
}