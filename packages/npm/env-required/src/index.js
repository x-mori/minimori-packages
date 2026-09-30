/**
 * Read required environment variables, failing once with every missing name.
 *
 * A variable counts as missing when it is absent or set to an empty string.
 * Validation happens before anything is returned, so startup code can fail fast
 * with one clear message instead of one error per variable.
 *
 * @example
 * const { DATABASE_URL, API_KEY } = envRequired(['DATABASE_URL', 'API_KEY']);
 * // Error: Missing required environment variables: DATABASE_URL, API_KEY
 *
 * // Any mapping works, which makes tests simple.
 * envRequired('PORT', { PORT: '8080' }); // { PORT: '8080' }
 *
 * @param names - One variable name, or an array of names.
 * @param source - Mapping to read; defaults to `process.env`.
 * @returns A new object with exactly the requested names and their string values.
 * @throws TypeError when names is empty or contains a non-string or empty name.
 * @throws Error listing every missing name when any variable is absent or empty.
 */
export function envRequired(names, source = process.env) {
  const required = typeof names === 'string' ? [names] : names;
  if (!Array.isArray(required) || required.some(name => typeof name !== 'string' || !name)) throw new TypeError('names must be a nonempty string or an array of names');
  const entries = [];
  const missing = [];
  for (const name of required) {
    const value = Object.hasOwn(source, name) ? source[name] : undefined;
    if (value === undefined || value === '') missing.push(name); else entries.push([name, value]);
  }
  if (missing.length) throw new Error(`Missing required environment variable${missing.length === 1 ? '' : 's'}: ${missing.join(', ')}`);
  // fromEntries defines own properties, so even a name like __proto__ stays data.
  return Object.fromEntries(entries);
}
