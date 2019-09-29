COUNTER = 'counter'
DELIVERIES = 'deliveries_map'
INITIATOR1 = ''
INITIATOR2 = ''
INITIATOR3 = ''
INITIATOR4 = ''
INITIATOR5 = ''

function init()
    hcreate('1')
    create_key(COUNTER)
    set_key(COUNTER, '0')
end

function create_delivery(initiator, from, to, description)
    local currentCounter = tostring(tonumber(get_key(COUNTER)) + 1)
    hset(currentCounter, 'id', currentCounter)
    hset(currentCounter, 'initiator', tostring(initiator))
    hset(currentCounter, 'deliverer', '')
    hset(currentCounter, 'from', tostring(from))
    hset(currentCounter, 'to', tostring(to))
    hset(currentCounter, 'description', tostring(description))
    hset(currentCounter, 'tokens', AUX)
end

function assign_delivery(mapId, deliverer)
    hset(tostring(mapId), 'deliverer', tostring(deliverer))
end

--function confirm_delivery(deliverer)

--end