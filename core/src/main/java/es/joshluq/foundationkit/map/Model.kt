package es.joshluq.foundationkit.map

/**
 * Marker interface representing a domain or presentation model within Clean Architecture.
 *
 * Extends [Mappable] to enable clean transformations between data models, domain models,
 * and UI models while maintaining strong type safety and consistency across modules.
 */
interface Model : Mappable
