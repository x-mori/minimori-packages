require 'minitest/autorun'
require 'stringio'
require 'x_mori/console_prefix'

class PackageTest < Minitest::Test
  def logger(io)
    XMori::ConsolePrefix.new('app', io: io, clock: -> { Time.utc(2026, 1, 15, 9, 30) })
  end

  def test_writes_prefixed_line
    io = StringIO.new
    line = logger(io).log('ok')
    assert_equal '[2026-01-15T09:30:00Z] [app] ok', line
    assert_equal "#{line}\n", io.string
  end

  def test_lines_from_threads_do_not_interleave
    io = StringIO.new
    log = logger(io)
    8.times.map { |n| Thread.new { 50.times { log.log("thread #{n}") } } }.each(&:join)
    lines = io.string.lines
    assert_equal 400, lines.size
    assert(lines.all? { |line| line.match?(/\A\[2026-01-15T09:30:00Z\] \[app\] thread \d\n\z/) })
  end
end
