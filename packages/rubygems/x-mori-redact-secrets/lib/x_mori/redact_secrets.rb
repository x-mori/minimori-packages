# frozen_string_literal: true

module XMori
  # Hides likely credentials in nested Ruby data before it is logged or reported.
  #
  # @example
  #   require 'x_mori/redact_secrets'
  #   XMori::RedactSecrets.call(
  #     { user: 'ada', password: 'hunter2', headers: { 'Authorization' => 'Bearer abc' }, note: 'Bearer xyz' }
  #   )
  #   # => { user: "ada", password: "[REDACTED]", headers: { "Authorization" => "[REDACTED]" }, note: "Bearer [REDACTED]" }
  module RedactSecrets
    # Hash keys whose values are replaced. Matched anywhere in the key, ignoring case,
    # so +password+, +db_password+, +access_token+, +client_secret+, +apiKey+, and +api-key+ all match.
    PATTERN = /(?:password|passwd|token|secret|api[_-]?key|authorization)/i
    BEARER = /Bearer\s+\S+/i
    REDACTED = '[REDACTED]'
    CIRCULAR = '[Circular]'
    private_constant :BEARER, :REDACTED, :CIRCULAR

    module_function

    # Returns a sanitized copy of a value.
    #
    # - Hash: copied; a value whose key (converted with +to_s+) matches PATTERN
    #   becomes <tt>"[REDACTED]"</tt>, and other values are sanitized recursively.
    #   Keys are kept unchanged.
    # - Array: copied, with every element sanitized.
    # - String: <tt>Bearer <token></tt> sequences become <tt>Bearer [REDACTED]</tt>, ignoring case.
    # - Anything else, including Structs and other objects, is returned as-is.
    #
    # A hash or array that contains itself, directly or indirectly, is replaced by
    # <tt>"[Circular]"</tt> at the point where it repeats. The input is never modified,
    # and hash subclasses and default values are not preserved in the copy.
    #
    # Redaction is pattern-based, so treat it as a safety net, not a guarantee:
    # secrets stored under unexpected keys or inside free text other than Bearer
    # tokens are not detected.
    #
    # @param value [Object] value to sanitize.
    # @param seen [Hash] internal traversal state; omit it.
    # @return [Object] the sanitized copy.
    def call(value, seen = {}.compare_by_identity)
      case value
      when Hash
        return CIRCULAR if seen.key?(value)

        seen[value] = true
        result = value.each_with_object({}) do |(key, item), out|
          out[key] = PATTERN.match?(key.to_s) ? REDACTED : call(item, seen)
        end
        seen.delete(value)
        result
      when Array
        return CIRCULAR if seen.key?(value)

        seen[value] = true
        result = value.map { |item| call(item, seen) }
        seen.delete(value)
        result
      when String
        value.gsub(BEARER, 'Bearer [REDACTED]')
      else
        value
      end
    end
  end
end
