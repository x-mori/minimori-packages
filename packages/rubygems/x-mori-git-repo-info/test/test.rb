require 'minitest/autorun'
require 'x_mori/git_repo_info'

class PackageTest < Minitest::Test
  EXPECTED = { owner: 'x-mori', repo: 'minimori-packages' }.freeze

  def test_parses_every_github_remote_form
    [
      'git@github.com:x-mori/minimori-packages.git',
      'git@github.com:x-mori/minimori-packages',
      'https://github.com/x-mori/minimori-packages',
      'https://github.com/x-mori/minimori-packages.git',
      'https://github.com/x-mori/minimori-packages/',
      'HTTPS://GitHub.com/x-mori/minimori-packages',
      'ssh://git@github.com/x-mori/minimori-packages.git',
      'git://github.com/x-mori/minimori-packages.git'
    ].each { |remote| assert_equal EXPECTED, XMori::GitRepoInfo.parse(remote), remote }
  end

  def test_keeps_dots_in_repository_names
    assert_equal({ owner: 'a', repo: 'my.site' }, XMori::GitRepoInfo.parse('https://github.com/a/my.site.git'))
  end

  def test_rejects_other_hosts_and_malformed_remotes
    ['https://gitlab.com/a/b', 'https://github.com/only-owner', 'https://github.com/a/b/c', 'not a url', 'http://github.com/a/b', nil].each do |remote|
      assert_raises(ArgumentError, remote.inspect) { XMori::GitRepoInfo.parse(remote) }
    end
  end
end
