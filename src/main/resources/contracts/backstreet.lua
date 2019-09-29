DELIVERY = 'delivery'

DELIVERER = ''

STATUS_OPEN = 'open'
STATUS_IN_PROGRESS = 'in_progress'
STATUS_FINISHED = 'finished'

function init()
    hcreate(DELIVERY)
end

function create_delivery(from, to, description, tokens)
    hset(DELIVERY, 'from', tostring(from))
    hset(DELIVERY, 'to', tostring(to))
    hset(DELIVERY, 'description', tostring(description))
    hset(DELIVERY, 'tokens', tostring(tokens))
    hset(DELIVERY, 'status', STATUS_OPEN)
end

function assign_delivery()
    DELIVERER = exec_account()
    hset(DELIVERY, 'status', STATUS_IN_PROGRESS)
end

function confirm_delivery()
    grant(DELIVERER, tonumber(hget(DELIVERY, 'tokens')))
    hset(DELIVERY, 'status', STATUS_FINISHED)
end