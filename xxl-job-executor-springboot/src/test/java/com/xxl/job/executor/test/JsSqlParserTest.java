package com.xxl.job.executor.test;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * @author yanpf
 * @date 2018/2/6 11:10
 * @description
 */
public class JsSqlParserTest {

    public static void main(String[] args) throws Exception  {
        String sql = "select project_id \"projectID\", project_name \"projectNM\", max(person_number) \"houseNum\", max(total_inc) \"totalReg\", round(case when coalesce(max(person_number),0)>0 then sum(current_inc)/(max(person_number)+0.00)else 0 end,2) \"permeRate\", max(total_bind_member) \"totalBinMem\", max(total_bind_room) \"totalBinHouse\", round(case when coalesce(max(person_number),0)>0 then sum(current_bind_room)/(max(person_number)+0.00)else 0 end,2) \"bindRate\", round(avg(current_act_mem),2) \"DAU\", round(case when coalesce(max(total_inc),0)>0 then sum(current_act_mem/(total_inc+0.00))/(To_date('2018-02-04' , 'yyyy-mm-dd') - To_date('2018-02-04' , 'yyyy-mm-dd')+1) else 0 end,2) \"DAURate\" from dwp.dwp_daily_shequ_data where dt between '2018-02-04' and '2018-02-04' and project_id=any('{6161125697621074,6161027652181364}') group by project_id,project_name order by project_name limit 10 offset 2";
        String sqlCount = "select count(*) from dwp.dwp_daily_shequ_data where dt between '2018-02-04' and '2018-02-04' and project_id=any('{6161125697621074,6161027652181364}') group by project_id,project_name order by project_name limit 10 offset 2";
        String sqlJoin = "SELECT a.\"id\", b.\"query_content\" FROM ds.ds_common_data_summary a JOIN (SELECT id, common_data_summary_id, query_content from ds.ds_common_data_sql) b on a.ID = b.common_data_summary_id ORDER BY a.id limit 10 offset 5";
        String sqlxx = "select project_id \"projectID\", project_name \"projectNM\", max(person_number) \"houseNum\", max(total_inc) \"totalReg\", round(case when coalesce(max(person_number),0)>0 then sum(current_inc)/(max(person_number)+0.00)else 0 end,2) \"permeRate\", max(total_bind_member) \"totalBinMem\", max(total_bind_room) \"totalBinHouse\", round(case when coalesce(max(person_number),0)>0 then sum(current_bind_room)/(max(person_number)+0.00)else 0 end,2) \"bindRate\", round(avg(current_act_mem),2) \"DAU\", round(case when coalesce(max(total_inc),0)>0 then sum(current_act_mem/(total_inc+0.00))/(To_date('2018-02-04' , 'yyyy-mm-dd') - To_date('2018-02-04' , 'yyyy-mm-dd')+1) else 0 end,2) \"DAURate\" from dwp.dwp_daily_shequ_data where dt between '2018-02-04' and '2018-02-04' and project_id=any('{6161125697621074,6161027652181364}') group by project_id,project_name order by project_name limit 1 offset 1 ";
        Statement statement = CCJSqlParserUtil.parse(sqlxx);
        Select select = ((Select) statement);
        SelectBody selectBody = select.getSelectBody();
        //SelectVisitor visitor = new
        if(selectBody instanceof PlainSelect){
            PlainSelect plainSelect = ((PlainSelect) selectBody);
            plainSelect.setLimit(null);
            plainSelect.setOffset(null);
            plainSelect.setOrderByElements(null);
            StringBuilder sb = new StringBuilder();
            StatementDeParser parser = new StatementDeParser(sb);
            select.setSelectBody(plainSelect);

            if(plainSelect.getGroupByColumnReferences() == null){
                plainSelect.getSelectItems().clear();
                Function count = new Function();
                count.setName("count");
                count.setAllColumns(true);
                plainSelect.getSelectItems().add(new SelectExpressionItem(count));
                select.accept(parser);
                System.out.println(parser.getBuffer().toString());
            }else{
                select.accept(parser);
                System.out.println("select count(*) from (" + parser.getBuffer().toString() + ") t");
            }


        }
    }
}
