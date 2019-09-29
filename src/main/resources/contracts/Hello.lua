function init()
    hcreate('hmap')
end

function opt_map(key, value)
    hset('hmap', tostring(key), tostring(value))
end