create or replace procedure get_mitkc_all_units(
cur_generic OUT result_sets.cur_generic
)
IS
BEGIN
  OPEN cur_generic for
  select t1.unit_number,t1.unit_name
  from unit t1 
  order by lower(t1.UNIT_NAME) asc;
END;
/