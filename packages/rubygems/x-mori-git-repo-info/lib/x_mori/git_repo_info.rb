# frozen_string_literal: true

require 'uri'

module XMori
  # Extracts the GitHub owner and repository name from a Git remote URL.
  #
  # @example
  #   require 'x_mori/git_repo_info'
  #   XMori::GitRepoInfo.parse('git@github.com:x-mori/minimori-packages.git')
  #   # => { owner: "x-mori", repo: "minimori-packages" }
  #
  #   remote = `git remote get-url origin`.strip
  #   info = XMori::GitRepoInfo.parse(remote)
  #   puts "https://github.com/#{info[:owner]}/#{info[:repo]}"
  module GitRepoInfo
    SCP_PREFIX = /\Agit@github\.com:/i
    OWNER_AND_REPO = %r{\A([A-Za-z0-9-]+)/([A-Za-z0-9_.-]+?)(?:\.git)?/?\z}
    SCHEMES = %w[https ssh git].freeze
    private_constant :SCP_PREFIX, :OWNER_AND_REPO, :SCHEMES

    module_function

    # Parses a github.com remote URL.
    #
    # Accepted forms, each with or without a trailing +.git+ and a trailing slash:
    # - HTTPS: <tt>https://github.com/owner/repo</tt>
    # - SSH URL: <tt>ssh://git@github.com/owner/repo.git</tt>
    # - scp-style SSH: <tt>git@github.com:owner/repo.git</tt>
    # - Git protocol: <tt>git://github.com/owner/repo.git</tt>
    #
    # The host is matched case-insensitively. Owner and repository names keep their
    # original case. Hosts other than github.com, including GitHub Enterprise
    # Server, are rejected.
    #
    # @param remote [String] remote URL, for example the output of <tt>git remote get-url origin</tt>.
    # @return [Hash{Symbol => String}] a new hash with +:owner+ and +:repo+ keys.
    # @raise [ArgumentError] if remote is not a String, is not a github.com remote,
    #   or has no owner/repository path.
    def parse(remote)
      raise ArgumentError, 'remote is required' unless remote.is_a?(String)

      path = if remote.match?(SCP_PREFIX)
               remote.sub(SCP_PREFIX, '')
             else
               uri = URI.parse(remote)
               raise ArgumentError, 'expected github.com' unless uri.host&.casecmp?('github.com') && SCHEMES.include?(uri.scheme)

               uri.path.delete_prefix('/')
             end
      match = OWNER_AND_REPO.match(path)
      raise ArgumentError, 'invalid GitHub remote' unless match

      { owner: match[1], repo: match[2] }
    rescue URI::InvalidURIError
      raise ArgumentError, 'invalid GitHub remote'
    end
  end
end
