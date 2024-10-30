-- 将用户能参与的活动存入缓存
for i = 1, #ARGV do
    if (i == #ARGV) then
        -- 设置有效期
        redis.call('EXPIREAT', ARGV[#ARGV])
    else
        redis.call('SADD', KEYS[1], ARGV[i])
    end
end

