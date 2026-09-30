Gem::Specification.new do |spec|
  spec.name = 'x-mori-once-async'
  spec.version = '1.1.0'
  spec.summary = 'Run a callable once across threads and share its result.'
  spec.description = spec.summary + ' This gem targets Ruby 3.1 or newer and has no runtime dependencies.'
  spec.authors = ['x-mori']
  spec.homepage = 'https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-once-async'
  spec.license = 'MIT'
  spec.required_ruby_version = '>= 3.1'
  spec.files = Dir['lib/**/*.rb', 'bin/*', 'README.md']
  spec.require_paths = ['lib']
  spec.metadata = { 'github_repo' => 'ssh://github.com/x-mori/minimori-packages', 'documentation_uri' => 'https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-once-async#readme', 'source_uri' => 'https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-once-async' }
  spec.bindir = 'bin'
  spec.executables = ['xmori-copy'] if spec.name == 'xmori-copy-to-clipboard-cli'
end
