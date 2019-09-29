--
-- Created by IntelliJ IDEA.
-- User: aroiggarcia
-- Date: 29/09/2019
-- Time: 10:51
-- To change this template use File | Settings | File Templates.
--
MAP_NAME = 'test'

DELIVERER = 'deliverer'

function init()

    --    create id counter
    create_key('id')
    set_key('id', '1')


    hcreate('master_map')
    hset('master_map', 'id', 'works')

    hcreate(MAP_NAME)
    --    hset('test', 'test1', 'test2')

end

function get_last_id(value)
    set_key('id', tostring(value + 1))
end

function create_delivery(value)
    hset(MAP_NAME, 'test1', tostring(value))
end

--function add_deliverer(id, deliverer)
--    hset('test', 'test1', 'test2')
--end