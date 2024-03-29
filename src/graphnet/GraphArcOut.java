/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphnet;

import PetriObj.ArcOut;
import graphpresentation.GraphArc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Inna
 */
public class GraphArcOut extends GraphArc implements Serializable {

    private static ArrayList<GraphArcOut> graphArcOutList = new ArrayList<>();  // added by Olha 24.09.12, cjrrect by Inna 28.11.2012
    private ArcOut arc;

    public GraphArcOut() { // додано Олею 28.09.12 для створення тимчасової дуги (тільки для промальовки) 
        super();
        arc = new ArcOut();
        //System.out.println("GraphTieOut  "+ arc.getNameT()+"  "+arc.getNumT()+"  "+arc.getNameP()+"  "+arc.getNumP());
        super.setLineWidth(1);
        super.setColor(Color.BLACK);
    }

    public GraphArcOut(ArcOut arcout) {
        arc = arcout;
        super.setLineWidth(1);
        super.setColor(Color.BLACK);

    }

    public ArcOut getArcOut() {
        return arc;
    }

    @Override
    public void setPetriElements() {
        arc.setQuantity(arc.getQuantity()); //???навіщо?
        arc.setNumT(super.getBeginElement().getNumber());
        arc.setNameT(super.getBeginElement().getName());
        arc.setNumP(super.getEndElement().getNumber());
        arc.setNameP(super.getEndElement().getName());
        /*  System.out.println("GraphTIE OUT : setPetriElements "+super.getBeginElement().getName()+  "  "+ super.getBeginElement().getNumber()+
                    super.getEndElement().getName()+"  "+super.getEndElement().getNumber()     
                );*/
        addElementToArrayList(); //// added by Olha 24.09.12
    }

    @Override
    public void addElementToArrayList() {   // added by Olha 24.09.12
        if (graphArcOutList == null) {
            graphArcOutList = new ArrayList<>();
        }
        graphArcOutList.add(this);
    }

    @Override
    public void drawGraphElement(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(getLineWidth()));
        g2.setColor(getColor());
        g2.draw(this.getGraphElement());
        drawArrowHead(g2);
        if (arc.getQuantity() != 1 || arc.kIsParam()) {
            String quantityString = arc.kIsParam() // added by Katya 08.12.2016
                    ? arc.getKParamName()
                    : Integer.toString(arc.getQuantity());
            this.getAvgLine().setLocation((this.getGraphElement().getX1() + this.getGraphElement().getX2()) / 2, (this.getGraphElement().getY1() + this.getGraphElement().getY2()) / 2);
            g2.drawLine((int) this.getAvgLine().getX() + 5, (int) this.getAvgLine().getY() - 5, (int) this.getAvgLine().getX() - 5, (int) this.getAvgLine().getY() + 5);
            g2.drawString(quantityString, (float) this.getAvgLine().getX(), (float) this.getAvgLine().getY() - 7);
        }
        if(this.isFirstArc()||this.isSecondArc()){ // важливо для правильної відмальовки після запуску мережі   
            this.updateCoordinates();
        }
    }

    public static ArrayList<GraphArcOut> getGraphArcOutList() {
        return graphArcOutList;
    }

    public static ArrayList<ArcOut> getArcOutList() {  // added by Inna 1.11.2012

        ArrayList<ArcOut> arrayTieOut = new ArrayList<>();
        for (GraphArcOut e : graphArcOutList) {
            arrayTieOut.add(e.getArcOut());
        }
        return arrayTieOut;
    }
//    public static void setTieOutList(ArrayList<TieOut> TieOutList) {
//        TieOut.tieOutList = TieOutList;
//    }

    public static void setNullTieOutList() {
        graphArcOutList.clear();
    }

    public static void addGraphTieOutList(List<GraphArcOut> tieOut) { // added by Olha 14/11/2012
        for (GraphArcOut to : tieOut) {
            graphArcOutList.add(to);
        }
    }

    @Override
    public int getQuantity() {  //потрібно для правильної роботи методу getQuantity() батьківського класу
        return arc.getQuantity();
    }

    @Override
    public void setQuantity(int i) {
        arc.setQuantity(i);
    }

}
