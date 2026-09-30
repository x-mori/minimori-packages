# frozen_string_literal: true

require 'rubygems'

module XMori
  # Looks up the installed version of a Ruby gem.
  #
  # @example
  #   require 'x_mori/package_version'
  #   XMori::PackageVersion.of('rake') # => "13.2.1"
  #
  #   # Inside `bundle exec`, the version Bundler resolved is returned.
  #   puts "Running with rails #{XMori::PackageVersion.of('rails')}"
  module PackageVersion
    module_function

    # Returns the version of an installed gem as a string.
    #
    # The gem is found with <tt>Gem::Specification.find_by_name</tt>, which respects
    # the gems activated by Bundler. When several versions are installed and none is
    # activated, the highest version is returned. The gem is not loaded.
    #
    # @param name [String] exact gem name, such as <tt>"rails"</tt>. Names are case-sensitive.
    # @return [String] the installed version, such as <tt>"7.1.3"</tt>.
    # @raise [ArgumentError] if name is not a non-empty String.
    # @raise [Gem::MissingSpecError] (a Gem::LoadError) if the gem is not installed.
    def of(name)
      raise ArgumentError, 'gem name is required' unless name.is_a?(String) && !name.empty?

      Gem::Specification.find_by_name(name).version.to_s
    end
  end
end
