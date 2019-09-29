COUNTER = 'counter'
DELIVERIES = 'deliveries_map'

function init()
    hcreate('1')
    hcreate('2')
    hcreate('3')
    hcreate('4')
    hcreate('5')
    create_key(COUNTER)
    set_key(COUNTER, '1')
end

function create_delivery(initiator, from, to, description, tokens)
    local currentCounter = get_key(COUNTER)
    hset(currentCounter, 'id', currentCounter)
    set_key(COUNTER, tostring(tonumber(currentCounter) + 1))
    hset(currentCounter, 'initiator', tostring(initiator))
    hset(currentCounter, 'from', tostring(from))
    hset(currentCounter, 'to', tostring(to))
    hset(currentCounter, 'description', tostring(description))
    hset(currentCounter, 'tokens', tostring(tokens))
end