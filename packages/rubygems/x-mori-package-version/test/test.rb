require 'minitest/autorun'
require 'x_mori/package_version'

class PackageTest < Minitest::Test
  def test_returns_installed_version
    assert_match(/\A\d/, XMori::PackageVersion.of('minitest'))
    assert_equal Gem::Specification.find_by_name('minitest').version.to_s, XMori::PackageVersion.of('minitest')
  end

  def test_raises_for_missing_gems
    assert_raises(Gem::LoadError) { XMori::PackageVersion.of('x-mori-no-such-gem') }
  end

  def test_rejects_invalid_names
    assert_raises(ArgumentError) { XMori::PackageVersion.of('') }
    assert_raises(ArgumentError) { XMori::PackageVersion.of(nil) }
    assert_raises(ArgumentError) { XMori::PackageVersion.of(:minitest) }
  end
end
