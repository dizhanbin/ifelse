/*
 * Copyright 1999-2019 fclassroom Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ifelse.vl;


import com.alibaba.fastjson.annotation.JSONField;
import com.intellij.openapi.project.Project;
import org.ifelse.model.MProperty;
import org.ifelse.model.MVar;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class VLItem {


    public String id;

    @JSONField(serialize = false)
    public boolean foucs;
    public String classname;

    public int width=40,height=40;
    public int x, y;

    public List<MProperty> mproperties;

    //执行流程节点
    public boolean is_run_point;
    public String next_run_point_id;

    public VLItem(){

        classname = this.getClass().getName();
        mproperties = new ArrayList<>();

    }


    public void setXY(int x,int y){

        this.x = x;
        this.y = y;


    }

    public abstract void paint(Graphics2D g);

    @JSONField(serialize = false)
    public boolean isPointIn(Point p){
        return ( x < p.x && x+width > p.x
                && y < p.y && y+height > p.y );
    }

    @JSONField(serialize = false)
    public boolean isFocus(){
        return foucs;
    }

    public void setFocus(boolean f){
        foucs = f;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    @JSONField(serialize = false)
    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }

    @JSONField(serialize = false)
    public boolean isNull(String v){

        return v==null||v.length()==0;
    }


    @JSONField(serialize = false)
    public String getDescript(){

        for(MProperty mProperty: mproperties){
            if( mProperty.key.equals("descript") )
                return mProperty.value;
        }

        return "";
    }


    public void initUI(Project project){

    };

    public boolean isLine(){

        return false;
    }


    public MProperty getMProperty(String key) {

        for(MProperty mProperty: mproperties){
            if( mProperty.key.equals(key) )
                return mProperty;
        }
        return null;
    }

    @Override
    public String toString() {
        return getDescript();
    }


    public abstract VLItem clone();


    @JSONField(serialize = false)
    public java.util.List<MVar> getVars() {




        if( isLine() )
            return null;
        List<MVar> result = new ArrayList<>();

        for(MProperty mp : mproperties){

            if( mp.name.equals("descript"))
                continue;
            if( mp.value != null && mp.value.indexOf('#')>-1 ){


                // Log.console("MVar find one%s",mp.value);
                String[] vs = mp.value.split(",");

                for(String v: vs){

                    if( v.charAt(0) == '#' ) {

                        String vv = getVar(v);

                        MVar var = new MVar(vv);
                        var.name = mp.name;
                        var.descript = getDescript();

                        result.add(var);

                        // Log.console("MVar  ->%s", var );

                    }
                }



            }

        }

        return result;



    }

    @JSONField(serialize = false)
    public  String getVar(String str){

        if( str.charAt(0) == '#' ){

            for(int i=1;i<str.length();i++){

                char c = str.charAt(i);
                if( !( (c>='A' && c<= 'z') || c == '_' ) ) {

                    return str.substring(0,i);

                }


            }
            return str;

        }
        else
            return null;
    }
}
